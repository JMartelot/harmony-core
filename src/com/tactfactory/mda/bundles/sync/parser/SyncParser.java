/**
 * This file is part of the Harmony package.
 *
 * (c) Mickael Gaillard <mickael.gaillard@tactfactory.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.tactfactory.mda.bundles.sync.parser;

import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.IntegerLiteralExpr;
import japa.parser.ast.expr.MemberValuePair;
import japa.parser.ast.expr.NormalAnnotationExpr;
import japa.parser.ast.expr.StringLiteralExpr;

import java.util.List;

import com.tactfactory.mda.bundles.sync.annotation.Sync;
import com.tactfactory.mda.bundles.sync.meta.SyncMetadata;
import com.tactfactory.mda.meta.ClassMetadata;
import com.tactfactory.mda.meta.FieldMetadata;
import com.tactfactory.mda.parser.BaseParser;
import com.tactfactory.mda.utils.PackageUtils;

public class SyncParser extends BaseParser{
	private static final String SYNC = "sync";
	private static final String ANNOT_SYNC = PackageUtils.extractNameEntity(Sync.class);
	private static final String ANNOT_SYNC_LEVEL = "level";
	private static final String ANNOT_SYNC_MODE = "mode";
	private static final String ANNOT_SYNC_PRIORITY = "priority";

	@Override
	public void visitClass(final ClassOrInterfaceDeclaration n, final ClassMetadata meta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitClassAnnotation(final ClassMetadata cm, final AnnotationExpr fieldAnnot) {
		if (fieldAnnot.getName().toString().equals(ANNOT_SYNC)){
			// Parse Sync arguments
			final SyncMetadata sm = new SyncMetadata();
			if (fieldAnnot instanceof NormalAnnotationExpr) {
				final NormalAnnotationExpr norm = (NormalAnnotationExpr)fieldAnnot;
				final List<MemberValuePair> pairs = norm.getPairs();
				for (final MemberValuePair pair : pairs){
					if (pair.getName().equals(ANNOT_SYNC_PRIORITY)){
						//TODO : Generate warning if type not recognized
						int priority  = 0;
						
						if (pair.getValue() instanceof IntegerLiteralExpr) {
							priority = Integer.parseInt(((IntegerLiteralExpr)pair.getValue()).getValue());
						} else if (pair.getValue() instanceof StringLiteralExpr) {
							priority = Sync.Priority.fromName(((StringLiteralExpr)pair.getValue()).getValue());
						} else {
							priority = Sync.Priority.fromName(pair.getValue().toString());
						}
						
						sm.priority= priority;
					} else
					
					if (pair.getName().equals(ANNOT_SYNC_MODE)){
						String mode = "";
						if (pair.getValue() instanceof StringLiteralExpr){
							mode = ((StringLiteralExpr)pair.getValue()).getValue();
						} else {
							mode = pair.getValue().toString();
						}
						sm.mode = Sync.Mode.fromName(mode);
					}
					
					if (pair.getName().equals(ANNOT_SYNC_LEVEL)){
						String level = "";
						if (pair.getValue() instanceof StringLiteralExpr){
							level = ((StringLiteralExpr)pair.getValue()).getValue();
						} else {
							level = pair.getValue().toString();
						}
						sm.level = Sync.Level.fromName(level);
					}
				}
			}
			cm.options.put(SYNC, sm);
			
			// Update fields of entity (fields of extends base)
			final FieldMetadata serverId = new FieldMetadata(cm);
			serverId.name = "serverId";
			serverId.columnName = "serverId";
			serverId.type = "integer";
			serverId.columnDefinition = "integer";
			serverId.hidden = true;
			serverId.nullable = true;
			cm.fields.put(serverId.name, serverId);
			
			final FieldMetadata sync_dtag = new FieldMetadata(cm);
			sync_dtag.name = "sync_dtag";
			sync_dtag.columnName = "sync_dtag";
			sync_dtag.type = "boolean";
			sync_dtag.columnDefinition = "boolean";
			sync_dtag.hidden = true;
			sync_dtag.nullable = false;
			cm.fields.put(sync_dtag.name, sync_dtag);
			
			final FieldMetadata sync_udate = new FieldMetadata(cm);
			sync_udate.name = "sync_uDate";
			sync_udate.columnName = "sync_uDate";
			sync_udate.type = "datetime";
			sync_udate.columnDefinition = "datetime";
			sync_udate.hidden = true;
			sync_udate.nullable = true;
			sync_udate.isLocale = true;
			cm.fields.put(sync_udate.name, sync_udate);
		}
	}

	@Override
	public void visitField(final FieldDeclaration field, final ClassMetadata meta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitFieldAnnotation(final FieldMetadata field,
			final AnnotationExpr fieldAnnot, final ClassMetadata meta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitMethod(final MethodDeclaration method, final ClassMetadata meta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitImport(final ImportDeclaration imp, final ClassMetadata meta) {
		// TODO Auto-generated method stub
		
	}

}
